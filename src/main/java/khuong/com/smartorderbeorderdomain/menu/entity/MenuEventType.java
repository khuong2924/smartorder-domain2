package khuong.com.smartorderbeorderdomain.menu.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MenuEventType {
    // Menu Item Events
    public static final String ITEM_CREATED = "ITEM_CREATED";                 // Khi tạo mới menu item
    public static final String ITEM_UPDATED = "ITEM_UPDATED";                 // Khi cập nhật thông tin menu item
    public static final String ITEM_DELETED = "ITEM_DELETED";                 // Khi xóa menu item
    public static final String ITEM_RESTORED = "ITEM_RESTORED";               // Khi khôi phục menu item đã xóa

    // Availability Events
    public static final String AVAILABILITY_CHANGED = "AVAILABILITY_CHANGED"; // Khi thay đổi trạng thái available
    public static final String ITEM_OUT_OF_STOCK = "ITEM_OUT_OF_STOCK";      // Khi hết hàng
    public static final String ITEM_BACK_IN_STOCK = "ITEM_BACK_IN_STOCK";    // Khi có hàng trở lại

    // Price Events
    public static final String PRICE_CHANGED = "PRICE_CHANGED";              // Khi thay đổi giá
    public static final String PRICE_INCREASED = "PRICE_INCREASED";          // Khi tăng giá
    public static final String PRICE_DECREASED = "PRICE_DECREASED";          // Khi giảm giá

    // Category Events
    public static final String CATEGORY_CREATED = "CATEGORY_CREATED";        // Khi tạo mới category
    public static final String CATEGORY_UPDATED = "CATEGORY_UPDATED";        // Khi cập nhật category
    public static final String CATEGORY_DELETED = "CATEGORY_DELETED";        // Khi xóa category

    // Special/Promotion Events
    public static final String SPECIAL_STARTED = "SPECIAL_STARTED";          // Khi bắt đầu khuyến mãi
    public static final String SPECIAL_ENDED = "SPECIAL_ENDED";              // Khi kết thúc khuyến mãi
    public static final String DISCOUNT_APPLIED = "DISCOUNT_APPLIED";        // Khi áp dụng giảm giá
    public static final String DISCOUNT_REMOVED = "DISCOUNT_REMOVED";        // Khi hủy giảm giá

    // Menu Option Events
    public static final String OPTION_ADDED = "OPTION_ADDED";               // Khi thêm option mới
    public static final String OPTION_UPDATED = "OPTION_UPDATED";          // Khi cập nhật option
    public static final String OPTION_REMOVED = "OPTION_REMOVED";          // Khi xóa option

    // Batch Update Events
    public static final String BATCH_UPDATE_STARTED = "BATCH_UPDATE_STARTED"; // Khi bắt đầu cập nhật hàng loạt
    public static final String BATCH_UPDATE_COMPLETED = "BATCH_UPDATE_COMPLETED"; // Khi hoàn thành cập nhật hàng loạt

    // Menu Sync Events
    public static final String MENU_SYNC_STARTED = "MENU_SYNC_STARTED";     // Khi bắt đầu đồng bộ menu
    public static final String MENU_SYNC_COMPLETED = "MENU_SYNC_COMPLETED"; // Khi hoàn thành đồng bộ menu
    public static final String MENU_SYNC_FAILED = "MENU_SYNC_FAILED";      // Khi đồng bộ menu thất bại

    // Cache Events
    public static final String CACHE_CLEARED = "CACHE_CLEARED";            // Khi xóa cache
    public static final String CACHE_UPDATED = "CACHE_UPDATED";           // Khi cập nhật cache

    private MenuEventType() {
        // Private constructor to prevent instantiation
    }

    /**
     * Kiểm tra xem một event type có hợp lệ không
     */
    public static boolean isValidEventType(String eventType) {
        try {
            Field[] fields = MenuEventType.class.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers()) &&
                        field.get(null).equals(eventType)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            return false;
        }
        return false;
    }

    /**
     * Lấy mô tả cho một event type
     */
    public static String getDescription(String eventType) {
        switch (eventType) {
            case ITEM_CREATED:
                return "Menu item has been created";
            case ITEM_UPDATED:
                return "Menu item has been updated";
            case ITEM_DELETED:
                return "Menu item has been deleted";
            case AVAILABILITY_CHANGED:
                return "Menu item availability has changed";
            case PRICE_CHANGED:
                return "Menu item price has changed";
            // Add more cases as needed
            default:
                return "Unknown event type";
        }
    }

    /**
     * Kiểm tra xem event có phải là event liên quan đến giá không
     */
    public static boolean isPriceEvent(String eventType) {
        return eventType.equals(PRICE_CHANGED) ||
                eventType.equals(PRICE_INCREASED) ||
                eventType.equals(PRICE_DECREASED);
    }

    /**
     * Kiểm tra xem event có phải là event liên quan đến availability không
     */
    public static boolean isAvailabilityEvent(String eventType) {
        return eventType.equals(AVAILABILITY_CHANGED) ||
                eventType.equals(ITEM_OUT_OF_STOCK) ||
                eventType.equals(ITEM_BACK_IN_STOCK);
    }
}
