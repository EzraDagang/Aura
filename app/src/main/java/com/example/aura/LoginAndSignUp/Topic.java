package com.example.aura.LoginAndSignUp;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class Topic {
    private String TopicName;

    public Topic(String name){
        setTopicName(name);
    }

    public void setTopicName(String topicName) {
        this.TopicName = topicName;
    }

    public String getTopicName() {
        return this.TopicName;
    }

    public ArrayList<Topic> setAllTopics(ArrayList<String> TopicName){
        ArrayList<Topic> AllTopics = new ArrayList<Topic>();

        for(String name : TopicName){
            Topic currentTopic = new Topic(name);
            AllTopics.add(currentTopic);
        }
        return AllTopics;
    }
}
