package edu.rosehulman.yangh4.roommate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PeopleInfo_Fragment extends Fragment {

    private static People person;
    private static String ARG_PERSON = "person_info_tag";

    public PeopleInfo_Fragment() {
        // Required empty public constructor
    }

    public static PeopleInfo_Fragment newInstance(People person) {

        Bundle args = new Bundle();

        PeopleInfo_Fragment fragment = new PeopleInfo_Fragment();
        args.putParcelable(ARG_PERSON, person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            person = getArguments().getParcelable(ARG_PERSON);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people_info_, container, false);
        ((TextView) view.findViewById(R.id.person_name)).setText(person.getName());
        ((TextView) view.findViewById(R.id.person_phone)).setText(person.getPhone());
        ((TextView) view.findViewById(R.id.people_contact_info)).setText(person.getEmail() + "\n" + person.getExtracontactinfo());
        ((TextView) view.findViewById(R.id.person_key)).setText(person.getKey());
        ((MainActivity) getActivity()).setActionBarTitle(person.getName());
        return view;
    }

}
