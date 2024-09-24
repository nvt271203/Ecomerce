package com.example.fashionecommerce.Helper;

import com.example.fashionecommerce.model.ItemSuggest;

import java.util.ArrayList;
import java.util.List;

public class DataSuggestHelper {
    public static boolean dataGugget_KeyWord(String textData){
        List<String> listSuggest = new ArrayList<>();
        listSuggest.add("Cửa hàng");
        listSuggest.add("Shop");
        listSuggest.add("Mua");
        for (int i =0; i<listSuggest.size(); i++){
            if (textData.toLowerCase().contains(listSuggest.get(i).toLowerCase())){
                return true;
            }
        }
        return false;
    }












//train name
    public static List<ItemSuggest> suggest_NAME(String name){
        List<ItemSuggest> listSuggest = new ArrayList<>();
        listSuggest.add(new ItemSuggest("trainAPP","Những sản phẩm " + name + " được bán trong shop ?"));
        listSuggest.add(new ItemSuggest("trainAI", "Tham khảo sản phẩm " + name + " trên thị trường ?"));
        return listSuggest;
    }

    public static List<ItemSuggest> suggest_CATEGORY(String category, String nameCategory){
        List<ItemSuggest> listSuggest = new ArrayList<>();
        listSuggest.add(new ItemSuggest("trainAPP","Những loại " + category + " được bán trong shop ?"));
        listSuggest.add(new ItemSuggest("trainAI", "Tham khảo "+ category +" trên thị trường ?"));
        listSuggest.add(new ItemSuggest("trainNoData", "Bạn muốn mua "+ category + " "+nameCategory +" trong shop ?"));
        return listSuggest;
    }
    public static List<ItemSuggest> suggest_CATEGORY_NAME(String category, String name){
        List<ItemSuggest> listSuggest = new ArrayList<>();
        listSuggest.add(new ItemSuggest("trainAPP","Những loại " + category.toLowerCase() + " " + name + " được bán trong shop ?"));
        listSuggest.add(new ItemSuggest("trainAI", "Tham khảo "+ category.toLowerCase() + " " + name +" trên thị trường ?"));
        return listSuggest;
    }















//    public static List<String> suggest_PRICE(int price){
//        List<String> listSuggest = new ArrayList<>();
//        listSuggest.add("Những sản phẩm có giá " + price + " được bán trong shop ?");
//        listSuggest.add("Shop có kinh doanh mặt hàng nào có giá " + price + " không ?");
//        return listSuggest;
//    }
//    public static List<String> suggest_CATEGORY_PRICE(String category, int price){
//        List<String> listSuggest = new ArrayList<>();
//        listSuggest.add("Những loại " + category + " có giá " + price + " được bán trong shop ?");
//        listSuggest.add( category + " có giá " + price + " còn hàng không ?");
//        return listSuggest;
//    }
//    public static List<String> suggest_NAME_PRICE(String name, int price){
//        List<String> listSuggest = new ArrayList<>();
//        listSuggest.add("Những sản phẩm " + name + " với giá " + price + " được bán trong shop ?");
//        listSuggest.add("Sản phẩm " + name + " với giá "+ price +" còn hàng không ?");
//        return listSuggest;
//    }
//    public static List<String> suggest_CATEGORY_NAME_PRICE(String category, String name, int price){
//        List<String> listSuggest = new ArrayList<>();
//        listSuggest.add("Những loại "+ category +" có tên " + name + " với giá " + price + " được bán trong shop ?");
//        listSuggest.add("Loại "+ category +" có tên " + name + " với giá " + price + " còn hàng không ?");
//        return listSuggest;
//    }


}