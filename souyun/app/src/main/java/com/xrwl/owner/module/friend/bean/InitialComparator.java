package com.xrwl.owner.module.friend.bean;

import java.util.Comparator;

public class InitialComparator<T extends IndexableEntity> implements Comparator<EntityWrapper<T>> {
        @Override
        public int compare(EntityWrapper<T> lhs, EntityWrapper<T> rhs) {
            return lhs.getIndex().compareTo(rhs.getIndex());
        }
    }