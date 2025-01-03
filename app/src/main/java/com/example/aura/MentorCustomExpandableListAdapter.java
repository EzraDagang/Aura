package com.example.aura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MentorCustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupList;
    private HashMap<String, List<String>> childMap;

    public MentorCustomExpandableListAdapter(Context context, List<String> groupList, HashMap<String, List<String>> childMap) {
        this.context = context;
        this.groupList = groupList;
        this.childMap = childMap;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childMap.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMap.get(groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_group, null);
        }

        TextView moduleTitle = convertView.findViewById(R.id.moduleTitle);

        // Prepend numbering to the module title
        String moduleName = groupList.get(groupPosition);
        moduleTitle.setText(String.format("Module %d: %s", groupPosition + 1, moduleName));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_child, null);
        }

        TextView lessonTitle = convertView.findViewById(R.id.lessonTitle);

        // Prepend numbering to the lesson title
        String lessonName = childMap.get(groupList.get(groupPosition)).get(childPosition);
        lessonTitle.setText(String.format("Lesson %d: %s", childPosition + 1, lessonName));

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
