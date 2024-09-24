package com.example.fashionecommerce.Helper;
import com.example.fashionecommerce.model.Locality;

import java.util.ArrayList;
import java.util.List;

public class LocalityListHelper {
    public static List<Locality> districtList(String idProvince){
        List<Locality> localityList = new ArrayList<>();
        switch (idProvince){
            case "HN":
                localityList.add(new Locality("TP. Hà Nội", "Quận Ba Đình"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Hoàn Kiếm"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Tây Hồ"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Long Biên"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Cầu Giấy"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Đống Đa"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Hai Bà Trưng"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Hoàng Mai"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Thanh Xuân"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Sóc Sơn"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Đông Anh"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Gia Lâm"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Nam Từ Liêm"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Thanh Trì"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Bắc Từ Liêm"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Mê Linh"));
                localityList.add(new Locality("TP. Hà Nội", "Quận Hà Đông"));
                localityList.add(new Locality("TP. Hà Nội", "Thị xã Sơn Tây"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Ba Vì"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Phúc Thọ"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Đan Phượng"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Hoài Đức"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Quốc Oai"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Thạch Thất"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Chương Mỹ"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Thanh Oai"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Thường Tín"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Phú Xuyên"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Ứng Hòa"));
                localityList.add(new Locality("TP. Hà Nội", "Huyện Mỹ Đức"));
                break;
            case "DN":
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Liên Chiểu"));
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Thanh Khê"));
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Hải Châu"));
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Sơn Trà"));
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Ngũ Hành Sơn"));
                localityList.add(new Locality("TP. Đà Nẳng", "Quận Cẩm Lệ"));
                localityList.add(new Locality("TP. Đà Nẳng", "Huyện Hòa Vang"));
                localityList.add(new Locality("TP. Đà Nẳng", "Huyện Hoàng Sa"));
                break;
            case "HCM":
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 1"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 12"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Thủ Đức"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 9"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Gò Vấp"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Bình Thạnh"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Tân Bình"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Tân Phú"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Phú Nhuận"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 2"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 3"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 10"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 11"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 4"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 5"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 6"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 8"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận Bình Tân"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Quận 7"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Huyện Củ Chi"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Huyện Hóc Môn"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Huyện Bình Chánh"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Huyện Nhà Bè"));
                localityList.add(new Locality("TP. Hồ Chí Minh", "Huyện Cần Giờ"));
                break;
        }
        return localityList;
    }
}
