package itp220.group.project;

//PK's version

import java.util.*;

// Simply used to compare different posts/threads (since a Thread ISA Post)
// by their ratings... --PK

public class PostRatingsComparator implements Comparator<Post>{
	public PostRatingsComparator(){}
	@Override
        public int compare(Post a, Post b)
	{
            return a.compareRatings(b);
	}
}
