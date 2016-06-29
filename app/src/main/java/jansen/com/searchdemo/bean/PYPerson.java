package jansen.com.searchdemo.bean;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import jansen.com.searchdemo.pinyinsearch.model.PinyinUnit;

/**
 * Created Jansen on 2016/6/29.
 */
public class PYPerson implements Cloneable, Serializable {
    /**
     * SearchByNull:默认查找
     * SearchByName:依据名字属性查找
     */
    public enum SearchByType {
        SearchByNull, SearchByName
    }

    /**
     * 被包装的对象
     */
    private Person mPerson;
    /**
     * 属性索引，可以有多个
     * name:张三  mSortKey:ZHANG 张 SAN 三
     */
    private String mSortKey;    //sort key word
    /**
     *PinyinUnit:每个字符的拼音属性,是类库自动生成的
     * 张三:
     * "namePinyinUnits":[
     {
     "pinyin":true,
     "pinyinBaseUnitIndex":[
     {
     "number":"94264",
     "originalString":"张",
     "pinyin":"zhang"
     }
     ],
     "startPosition":0
     },
     {
     "pinyin":true,
     "pinyinBaseUnitIndex":[
     {
     "number":"726",
     "originalString":"三",
     "pinyin":"san"
     }
     ],
     "startPosition":1
     }
     */
    private List<PinyinUnit> mNamePinyinUnits; //mName converted to py characters
    /**
     * 搜索类型，可以是依据名字搜索，依据年龄搜索
     */
    private SearchByType mSearchByType;    //search type
    private StringBuffer mMatchKeywords;    //type of match keywords
    private int mMatchStartIndex;   //the match start  position of mMatchKeywords in original string(name or phoneNumber).
    private int mMatchLength;   //the match length of mMatchKeywords in original string(name or phoneNumber).
    private boolean mSelected;  //selected flag

    public PYPerson(Person mPerson) {
        this.mPerson = mPerson;
        setPerson(mPerson);
        setNamePinyinUnits(new ArrayList<PinyinUnit>());
        setSearchByType(SearchByType.SearchByNull);
        setMatchKeywords(new StringBuffer());
        getMatchKeywords().delete(0, getMatchKeywords().length());
        setMatchStartIndex(-1);
        setMatchLength(0);
        setSelected(false);
    }

    public PYPerson(Person mPerson, String mSortKey) {
        this.mPerson = mPerson;
        setPerson(mPerson);
        setSortKey(mSortKey);
        setNamePinyinUnits(new ArrayList<PinyinUnit>());
        setSearchByType(SearchByType.SearchByNull);
        setMatchKeywords(new StringBuffer());
        getMatchKeywords().delete(0, getMatchKeywords().length());
        setMatchStartIndex(-1);
        setMatchLength(0);
        setSelected(false);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            PYPerson obj = (PYPerson) super.clone();
            obj.mPerson = (Person) mPerson.clone();
            obj.mNamePinyinUnits = new ArrayList<PinyinUnit>();
            for (PinyinUnit pu : mNamePinyinUnits) {
                obj.mNamePinyinUnits.add((PinyinUnit) pu.clone());
            }
            obj.mSearchByType = mSearchByType;
            obj.mMatchKeywords = new StringBuffer(mMatchKeywords);
            return obj;
        } catch (CloneNotSupportedException mE) {
            mE.printStackTrace();
        }
        return super.clone();
    }

    private static Comparator<Object> mChineseComparator = Collator.getInstance(Locale.CHINA);

    public static Comparator<PYPerson> mSearchComparator = new Comparator<PYPerson>() {

        @Override
        public int compare(PYPerson lhs, PYPerson rhs) {
            int compareMatchStartIndex=(lhs.mMatchStartIndex-rhs.mMatchStartIndex);
            return ((0!=compareMatchStartIndex)?(compareMatchStartIndex):(rhs.mMatchLength-lhs.mMatchLength));
        }
    };

    public static Comparator<PYPerson> mAscComparator = new Comparator<PYPerson>() {
        @Override
        public int compare(PYPerson lhs, PYPerson rhs) {
            boolean flag = false;
            if(lhs.mSortKey != null && rhs.mSortKey != null){
                return mChineseComparator.compare(lhs.mSortKey, rhs.mSortKey);
            }else if(lhs.mSortKey == null && rhs.mSortKey == null){
                flag = true;
                return 100;
            }else if((lhs.mSortKey == null || rhs.mSortKey == null) && flag == false){
                return  -100;
            }
            return  0;
        }
    };

    public Person getPerson() {
        return mPerson;
    }

    public void setPerson(Person mPerson) {
        this.mPerson = mPerson;
    }

    public String getSortKey() {
        return mSortKey;
    }

    public void setSortKey(String mSortKey) {
        this.mSortKey = mSortKey;
    }

    public List<PinyinUnit> getNamePinyinUnits() {
        return mNamePinyinUnits;
    }

    public void setNamePinyinUnits(List<PinyinUnit> mNamePinyinUnits) {
        this.mNamePinyinUnits = mNamePinyinUnits;
    }

    public SearchByType getSearchByType() {
        return mSearchByType;
    }

    public void setSearchByType(SearchByType mSearchByType) {
        this.mSearchByType = mSearchByType;
    }

    public StringBuffer getMatchKeywords() {
        return mMatchKeywords;
    }

    public void setMatchKeywords(StringBuffer matchKeywords) {
        mMatchKeywords = matchKeywords;
    }

    public void setMatchKeywords(String matchKeywords) {
        mMatchKeywords.delete(0, mMatchKeywords.length());
        mMatchKeywords.append(matchKeywords);
    }


    public int getMatchStartIndex() {
        return mMatchStartIndex;
    }

    public void setMatchStartIndex(int mMatchStartIndex) {
        this.mMatchStartIndex = mMatchStartIndex;
    }

    public int getMatchLength() {
        return mMatchLength;
    }

    public void setMatchLength(int mMatchLength) {
        this.mMatchLength = mMatchLength;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
