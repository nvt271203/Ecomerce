package com.example.fashionecommerce.model;

public enum StatusOder {
    WAIT, SUCCESS, REFUSE;
    public static String getStatus(StatusOder status){
        String statusOder;
        switch (status){
            case WAIT:
                statusOder = "Chờ kiểm duyệt";
                break;
            case SUCCESS:
                statusOder = "Đã duyệt";
                break;
            default:
                statusOder = "Đơn hàng bị hủy";
                break;
        }
        return statusOder;
    }
}
