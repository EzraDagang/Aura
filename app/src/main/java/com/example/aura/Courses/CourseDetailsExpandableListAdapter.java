package com.example.aura.Courses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.aura.R;

import java.util.HashMap;
import java.util.List;

public class CourseDetailsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> moduleTitles; // List of module titles (groups)
    private HashMap<String, List<String>> lessonsMap; // Map of module titles to their lessons (children)

    public CourseDetailsExpandableListAdapter(Context context, List<String> moduleTitles, HashMap<String, List<String>> lessonsMap) {
        this.context = context;
        this.moduleTitles = moduleTitles;
        this.lessonsMap = lessonsMap;
    }

    @Override
    public int getGroupCount() {
        return moduleTitles.size(); // Number of modules
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String moduleTitle = moduleTitles.get(groupPosition);
        List<String> lessons = lessonsMap.get(moduleTitle);
        return (lessons != null) ? lessons.size() : 0; // Number of lessons in the module
    }

    @Override
    public Object getGroup(int groupPosition) {
        return moduleTitles.get(groupPosition); // Module title
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String moduleTitle = moduleTitles.get(groupPosition);
        List<String> lessons = lessonsMap.get(moduleTitle);
        return (lessons != null) ? lessons.get(childPosition) : null; // Lesson title
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
        String moduleTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null); // Inflate custom group layout
        }

        TextView groupTitle = convertView.findViewById(R.id.groupTitle);
        groupTitle.setText(moduleTitle); // Set the module title
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String lessonTitle = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item, null); // Inflate custom child layout
        }

        TextView childTitle = convertView.findViewById(R.id.childTitle);
        childTitle.setText(lessonTitle); // Set the lesson title
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
