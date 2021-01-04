package com.xrwl.owner.module.friend.bean;

import com.google.gson.annotations.SerializedName;
import com.ldw.library.utils.PinyinUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by www.longdw.com on 2018/3/28 下午8:55.
 */

public class Friend extends BaseContact implements IndexableEntity, Serializable {

    static final String INDEX_SIGN = "#";

    @SerializedName("tel")
    public String phone;
    @SerializedName("pic")
    public String avatar;
    @SerializedName("is_register")
    public String register;//是否已注册
    public String pinyin;

    public String is_registqubie;

    public boolean isRegister() {
        return "1".equals(register);
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getIs_registqubie() {
        return is_registqubie;
    }

    public void setIs_registqubie(String is_registqubie) {
        this.is_registqubie = is_registqubie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String getFieldIndexBy() {
        return this.name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        //需要用到拼音时(比如:搜索), 可增添pinyin字段 this.pinyin  = pinyin
        this.pinyin = pinyin;
    }

    /**
     * List<T> -> List<Indexable<T>
     */
    public static <T extends IndexableEntity> List<EntityWrapper<T>> transform(final List<T> datas) {
        try {
            TreeMap<String, List<EntityWrapper<T>>> map = new TreeMap<>((lhs, rhs) -> {
                if (lhs.equals(INDEX_SIGN)) {
                    return rhs.equals(INDEX_SIGN) ? 0 : 1;
                } else if (rhs.equals(INDEX_SIGN)) {
                    return -1;
                }
                return lhs.compareTo(rhs);
            });

            for (int i = 0; i < datas.size(); i++) {
                EntityWrapper<T> entity = new EntityWrapper<>();
                T item = datas.get(i);
                String indexName = item.getFieldIndexBy();
                String pinyin = PinyinUtil.getPingYin(indexName);
                entity.setPinyin(pinyin);

                // init EntityWrapper
                if (PinyinUtil.matchingLetter(pinyin)) {
                    entity.setIndex(pinyin.substring(0, 1).toUpperCase());
                    entity.setIndexByField(item.getFieldIndexBy());
                } else if (PinyinUtil.matchingPolyphone(pinyin)) {
                    entity.setIndex(PinyinUtil.gePolyphoneInitial(pinyin).toUpperCase());
                    entity.setPinyin(PinyinUtil.getPolyphoneRealPinyin(pinyin));
                    String hanzi = PinyinUtil.getPolyphoneRealHanzi(indexName);
                    entity.setIndexByField(hanzi);
                    // 把多音字的真实indexField重新赋值
                    item.setFieldIndexBy(hanzi);
                } else {
                    entity.setIndex(INDEX_SIGN);
                    entity.setIndexByField(item.getFieldIndexBy());
                }
                entity.setIndexTitle(entity.getIndex());
                entity.setData(item);
                entity.setOriginalPosition(i);
                item.setFieldPinyinIndexBy(entity.getPinyin());

                String inital = entity.getIndex();

                List<EntityWrapper<T>> list;
                if (!map.containsKey(inital)) {
                    list = new ArrayList<>();
                    list.add(new EntityWrapper<T>(entity.getIndex(), EntityWrapper.TYPE_TITLE));
                    map.put(inital, list);
                } else {
                    list = map.get(inital);
                }

                list.add(entity);
            }

            ArrayList<EntityWrapper<T>> list = new ArrayList<>();
            for (List<EntityWrapper<T>> indexableEntities : map.values()) {
                Comparator comparator = new InitialComparator<T>();
                Collections.sort(indexableEntities, comparator);
                list.addAll(indexableEntities);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
