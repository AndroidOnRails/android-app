package com.hana053.micropost.ui.pages.signup.password;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hana053.micropost.R;
import com.hana053.micropost.databinding.SignupPasswordBinding;
import com.hana053.micropost.interactors.UserInteractor;
import com.hana053.micropost.ui.pages.signup.SignupBaseFragment;
import com.hana053.micropost.ui.pages.signup.SignupViewModel;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;

public class SignupPasswordFragment extends SignupBaseFragment<SignupPasswordBinding> implements SignupPasswordViewListener {

    public static Fragment newInstance(SignupViewModel viewModel) {
        final SignupPasswordFragment fragment = new SignupPasswordFragment();
        fragment.setViewModelAsArguments(viewModel);
        return fragment;
    }

    @Override
    public View.OnClickListener onClickNextBtn() {
        return nextBtnHandler.call(ctrl -> {
            final Subscription subscription = getUtilityWrapper().getSignupService().signup(createSignupRequest())
                    .doOnSubscribe(getUtilityWrapper().getProgressBarHandler()::show)
                    .doAfterTerminate(getUtilityWrapper().getProgressBarHandler()::hide)
                    .subscribe(x -> getUtilityWrapper().getNavigator().navigateToMain(), e -> {
                        if (isEmailAlreadyTaken(e)) {
                            Toast.makeText(getContext(), "This email is already taken.", Toast.LENGTH_LONG).show();
                            ctrl.navigateToPrev();
                        } else {
                            getUtilityWrapper().getHttpErrorHandler().handleError(e);
                        }
                    });
            collectSubscription(subscription);
            return true;
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.signup_password, container, false);
    }

    @Override
    protected SignupPasswordBinding setupBinding(SignupViewModel viewModel) {
        SignupPasswordBinding binding = SignupPasswordBinding.bind(getView());
        binding.setViewModel(viewModel);
        binding.setListener(this);
        binding.setBtnHandler(getBtnHandler());
        return binding;
    }

    @Override
    protected boolean isFormValid() {
        return getPassword().length() >= 8;
    }

    @Override
    protected void saveViewModelState(SignupViewModel viewModel) {
        viewModel.password.set(getPassword());
    }

    @Override
    protected void showOrHideErrorMsg(boolean isValid) {
        if (isValid || getPassword().length() == 0)
            getBinding().passwordInvalid.setVisibility(View.GONE);
        else
            getBinding().passwordInvalid.setVisibility(View.VISIBLE);
    }

    @NonNull
    private String getPassword() {
        return getBinding().password.getText().toString();
    }

    private boolean isEmailAlreadyTaken(Throwable e) {
        return e instanceof HttpException && ((HttpException) e).code() == 400;
    }

    private UserInteractor.SignupRequest createSignupRequest() {
        return UserInteractor.SignupRequest.builder()
                .email(getViewModel().email.get())
                .password(getViewModel().password.get())
                .name(getViewModel().fullName.get())
                .build();
    }

}