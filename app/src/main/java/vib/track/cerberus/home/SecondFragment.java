package vib.track.cerberus.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import vib.track.cerberus.R;
import vib.track.cerberus.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // App settings button
        //binding.buttonCredits.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        NavHostFragment.findNavController(SettingsPage.this).navigate(R.id.action_SettingsPage_to_credits);
        //    }
        //});

    }

    //@Override
    //public void onDestroyView() {
    //    super.onDestroyView();
    //    binding = null;
    //}

}
