package com.tamn.app2uproject.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tamn.app2uproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    String strtext;

    public CommentFragment() {
        // Required empty public constructor
    }

    //todo: DELETE this fragment and it's xml

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //todo: delete this line
//        strtext = getArguments().getString("tam");
        // Inflate the layout for this fragment*/
        View inflate = inflater.inflate(R.layout.fragment_comment, container, false);
        Toast.makeText(getActivity(), "commenttttt", Toast.LENGTH_SHORT).show();
        //initEvents();
        return inflate;
    }

    /*private void initEvents() {
        Toast.makeText(getActivity(), strtext, Toast.LENGTH_SHORT).show();
        //strtext
    }*/

}
