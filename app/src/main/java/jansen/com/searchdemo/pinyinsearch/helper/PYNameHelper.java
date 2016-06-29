package jansen.com.searchdemo.pinyinsearch.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jansen.com.searchdemo.bean.PYPerson;
import jansen.com.searchdemo.bean.Person;
import jansen.com.searchdemo.pinyinsearch.model.PinyinUnit;
import jansen.com.searchdemo.pinyinsearch.util.PinyinUtil;
import jansen.com.searchdemo.pinyinsearch.util.T9MatchPinyinUnits;

/**
 * Created Jansen on 2016/6/29.
 */
public class PYNameHelper {
    private static PYNameHelper mInstance = null;
    private Context mContext;

    private List<PYPerson> mBasePersons = null; // 数据源
    private List<PYPerson> mSearchPersons = null; // 搜索结果

    private PYNameHelper() {
        initPersonsHelper();
    }

    public static PYNameHelper getInstance() {
        if (null == mInstance) {
            mInstance = new PYNameHelper();
        }
        return mInstance;
    }

    private void initPersonsHelper() {
        if (null == mBasePersons) {
            mBasePersons = new ArrayList<PYPerson>();
        } else {
            mBasePersons.clear();
        }

        if (null == mSearchPersons) {
            mSearchPersons = new ArrayList<PYPerson>();
        } else {
            mSearchPersons.clear();
        }

    }

    /**
     * 设置拼音搜索的源数据
     */
    public void setBaseGoods(List<Person> mPersons) {
        String sortkey = null;
        if (mPersons == null) {
            return;
        }
        mBasePersons = new ArrayList<PYPerson>();
        for (Person clb : mPersons) {
            PYPerson tmp = new PYPerson(clb);
            PinyinUtil.chineseStringToPinyinUnit(tmp.getPerson().getName(), tmp.getNamePinyinUnits());
            String sortkeyOriginal = PinyinUtil.getSortKey(tmp.getNamePinyinUnits());
            if (sortkeyOriginal != null) {
                sortkey = sortkeyOriginal.toUpperCase();
                tmp.setSortKey(praseSortKey(sortkey));
            }
            mBasePersons.add(tmp);
        }
        Collections.sort(mBasePersons, PYPerson.mAscComparator);
    }

    private String praseSortKey(String sortKey) {
        if (null == sortKey || sortKey.length() <= 0) {
            return null;
        }

        if ((sortKey.charAt(0) >= 'a' && sortKey.charAt(0) <= 'z')
                || (sortKey.charAt(0) >= 'A' && sortKey.charAt(0) <= 'Z')) {
            return sortKey;
        }

        return String.valueOf('#')
                + sortKey;
    }

    //模糊查询数据
    public List<PYPerson> parseT9InputSearchPerson(String search) {
        List<PYPerson> mSearchByNamePersons=new ArrayList<PYPerson>();
        /**
         * search by other properties;
         */
/*        List<PYPerson> mSearchByNamePersons=new ArrayList<PYPerson>();*/
        if (null == search) {// add all base data to search
            if (null != mSearchPersons) {
                mSearchPersons.clear();
            } else {
                mSearchPersons = new ArrayList<PYPerson>();
            }
            return null;
        }

        if (null != mSearchPersons) {
            mSearchPersons.clear();
        } else {
            mSearchPersons = new ArrayList<PYPerson>();   //最终要返回的集合
        }

        int CustomersCount = mBasePersons.size();

        /**
         * search process: 1:Search by name (1)Search by name pinyin
         * characters(org name->name pinyin characters) ('0'~'9')
         * (2)Search by org name ('0'~'9') 2:Search by phone number
         * ('0'~'9')
         */
        for (int i = 0; i < CustomersCount; i++) {
            List<PinyinUnit> pinyinUnits = mBasePersons.get(i).getNamePinyinUnits();
            StringBuffer chineseKeyWord = new StringBuffer();// In order to get Chinese KeyWords.Ofcourse it's maybe not Chinese characters.
            String name = mBasePersons.get(i).getPerson().getName();
            //$$$$$$$$$$$$$ 找到匹配字符串
            if (name == null) {
                continue;
            }
            /**
             * search by NamePinyinUnits;
             */
            if ( T9MatchPinyinUnits.matchPinyinUnits(pinyinUnits, name, search, chineseKeyWord)) {
                PYPerson currentPersons=mBasePersons.get(i);
                currentPersons.setSearchByType(PYPerson.SearchByType.SearchByName);

                currentPersons.setMatchKeywords(chineseKeyWord.toString());

                currentPersons.setMatchStartIndex(currentPersons.getPerson().getName().indexOf(currentPersons.getMatchKeywords().toString()));
                currentPersons.setMatchLength(currentPersons.getMatchKeywords().length());

                mSearchByNamePersons.add(currentPersons);
                chineseKeyWord.delete(0, chineseKeyWord.length());
                continue;

            }else {
                /**
                 * search by other properties;
                 */
            }
        }

        if(mSearchByNamePersons.size()>0){
            Collections.sort(mSearchByNamePersons, PYPerson.mSearchComparator);
        }
        /**
         * search by other properties;
         */
/*        if(mSearchByNamePersons.size()>0){
            Collections.sort(mSearchByNamePersons, PYPerson.mSearchComparator);
        }*/

        mSearchPersons.clear();
        mSearchPersons.addAll(mSearchByNamePersons);
        /**
         * search by other properties;
         */
/*        mSearchPersons.addAll(mSearchByNamePersons);*/
        Collections.sort(mSearchPersons, PYPerson.mAscComparator);
        return mSearchPersons;
    }
}
