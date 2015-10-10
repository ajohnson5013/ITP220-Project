/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itp220.group.project;

import java.util.*;

public class ThreadTopicComparator implements Comparator<Thread> {
    public ThreadTopicComparator(){}
    public int compare(Thread a, Thread b)
    {
        return a.compareTopics(b);
    }
}
