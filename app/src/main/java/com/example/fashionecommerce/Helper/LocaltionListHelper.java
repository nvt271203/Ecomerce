package com.example.fashionecommerce.Helper;

import com.example.fashionecommerce.model.Localtion;

import java.util.ArrayList;
import java.util.List;

public class LocaltionListHelper {
    public static List<Localtion> getLocaltionList(){
        List<Localtion> localityList = new ArrayList<>();
        localityList.add(new Localtion("HN", "Tp. Hà Nội"));
        localityList.add(new Localtion("DN", "Tp. Đà Nẳng"));
        localityList.add(new Localtion("HCM", "Tp. Hồ Chí Minh"));
        return localityList;
    }

}
