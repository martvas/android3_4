package ru.geekbrains.android3_4.presenter;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import ru.geekbrains.android3_4.model.entity.UserProjects;
import ru.geekbrains.android3_4.model.repo.UsersRepo;
import ru.geekbrains.android3_4.view.MainView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    Scheduler mainThreadShceduler;
    UsersRepo usersRepo;

    public MainPresenter(Scheduler mainThreadShceduler) {
        this.mainThreadShceduler = mainThreadShceduler;
        usersRepo = new UsersRepo();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadData();
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        usersRepo.getUser("martvas")
                .subscribeOn(Schedulers.io())
                .observeOn(mainThreadShceduler)
                .subscribe(user -> {

                    //TODO: получить и отобразить список репозиториев пользователя
                    usersRepo.getProjects(user.getReposUrl())
                            .subscribeOn(Schedulers.io())
                            .observeOn(mainThreadShceduler)
                            .subscribe(projects -> {
                                StringBuilder stringReposUrl = new StringBuilder();
                                for (UserProjects project : projects) {
                                    stringReposUrl.append((projects.indexOf(project) + 1) + ". " + project.getUrl() + ", \n");
                                }
                                getViewState().setReposText(String.valueOf(stringReposUrl));
                            }, throwable -> {
                                Timber.e(throwable, "Failed to get user projects");
                            });

                    getViewState().setUsernameText(user.getLogin());
                    getViewState().loadImage(user.getAvatarUrl());
                }, throwable -> {
                    Timber.e(throwable, "Failed to get user");
                });
    }

    private void getDataViaOkHttp() {
        Single<String> single = Single.fromCallable(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/users/AntonZarytski")
                    .build();
            return client.newCall(request).execute().body().string();
        });

        single.subscribeOn(Schedulers.io())
                .observeOn(mainThreadShceduler)
                .subscribe(s -> Timber.d(s));

    }
}
