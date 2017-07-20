package com.claymav.payday2assistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by maver on 6/25/2017.
 */

public class experience extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public experience() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static experience newInstance(String param1, String param2) {
        experience fragment = new experience();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_experience, container, false);
        Button upButton = (Button) view.findViewById(R.id.subbutton);
        upButton.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void updateTextView(View v) {
        int[] arbitraryLevels = new int[] {900, 1250, 1550, 1850, 2200, 2600, 3000, 3500, 4000, 4600};
        double startLevel = 0;
        double endLevel = 0;
        TextView output = (TextView)getView().findViewById(R.id.expneeded);
        int excess = 0;
        int answer = 0;
        String sadText = "";

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        String startString = ((EditText)getView().findViewById(R.id.fromlvl)).getText().toString();
        String endString = ((EditText)getView().findViewById(R.id.tolvl)).getText().toString();

        // Check if inputs are not empty strings, if fail, flag error
        if(startString.length() > 0 && endString.length() > 0) {
            startLevel = Double.parseDouble(startString);
            endLevel = Double.parseDouble(endString);
        }
        else {
            sadText = getResources().getString(R.string.error_text_exp);

            output.setText(sadText);
            Toast.makeText(getActivity(), "Please enter valid numbers",
                    Toast.LENGTH_SHORT).show();
        }

        // Check is inputs are valid
        if (startLevel < 0 || endLevel < 0 || startLevel > endLevel) {
            sadText = getResources().getString(R.string.error_text_exp);

            output.setText(sadText);
            Toast.makeText(getActivity(), "Please enter valid numbers",
                    Toast.LENGTH_SHORT).show();
        }
        else if(startLevel<10 && endLevel>10) {
            for (int i = 0; i < startLevel; i--) {
                excess += arbitraryLevels[i];
            }
            answer = totalExperienceCalculator(endLevel) - excess;
        }
        else if(startLevel<10 && endLevel<=10) {
            excess = 0;
            for (int i = (int)startLevel; i < endLevel; i++) {
                excess += arbitraryLevels[i];
            }
            answer = excess;
        }
        else {
            answer = totalExperienceCalculator(endLevel) - totalExperienceCalculator(startLevel);
        }

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String answerFormatted = formatter.format(answer);

        if (sadText.length() == 0) {
            output.setText(answerFormatted);
        }
    }

    private int totalExperienceCalculator(double level) {
        double squaredComponent = ((level - 10) * (level -9) / 2) * ((level - 10) * (level -9) / 2);
        return (int)((1.3654321 * squaredComponent + 4600 * (level - 10) + 25450) - 0.5);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subbutton:
                updateTextView(v);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}