package edu.uw.tcss450.lab3_authentication.ui.auth.register;

import static edu.uw.tcss450.lab3_authentication.utils.PasswordValidator.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.lab3_authentication.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.lab3_authentication.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {

    }

    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();

        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {

    }
}
