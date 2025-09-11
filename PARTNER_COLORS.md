# Bảng Màu Chủ Đạo cho Từng Đối Tác

## Tổng quan
Mỗi đối tác có bộ màu chủ đạo riêng để tạo sự khác biệt và phù hợp với thương hiệu:

## 🏦 Bank (Ngân hàng) - Màu Xanh Dương
- **Primary Color**: `#1976D2` (Xanh dương chính)
- **Primary Dark**: `#1565C0` (Xanh dương đậm)
- **Primary Light**: `#BBDEFB` (Xanh dương nhạt)
- **Accent Color**: `#FF4081` (Hồng accent)

## 🏛️ Government (Hành chính công) - Màu Cam
- **Primary Color**: `#FF9800` (Cam chính)
- **Primary Dark**: `#F57C00` (Cam đậm)
- **Primary Light**: `#FFE0B2` (Cam nhạt)
- **Accent Color**: `#FF5722` (Cam đỏ accent)

## ✈️ Airport (Sân bay) - Màu Xanh Lá
- **Primary Color**: `#4CAF50` (Xanh lá chính)
- **Primary Dark**: `#388E3C` (Xanh lá đậm)
- **Primary Light**: `#C8E6C9` (Xanh lá nhạt)
- **Accent Color**: `#8BC34A` (Xanh lá accent)

## Cấu trúc File
```
app/src/
├── bank/res/values/
│   ├── colors.xml      # Màu xanh dương
│   ├── themes.xml      # Theme cho bank
│   └── strings.xml     # Strings cho bank
├── government/res/values/
│   ├── colors.xml      # Màu cam
│   ├── themes.xml      # Theme cho government
│   └── strings.xml     # Strings cho government
└── airport/res/values/
    ├── colors.xml      # Màu xanh lá
    ├── themes.xml      # Theme cho airport
    └── strings.xml     # Strings cho airport
```

## Cách sử dụng trong code
```xml
<!-- Trong layout XML -->
<Button
    android:background="@color/primary_color"
    android:textColor="@color/text_primary" />

<!-- Trong code Java/Kotlin -->
int primaryColor = getResources().getColor(R.color.primary_color);
```

## Lưu ý
- Màu sắc sẽ tự động áp dụng khi build từng flavor
- Có thể tùy chỉnh thêm màu trong file `colors.xml` của từng đối tác
- Theme sẽ override theme mặc định trong `main/res/values/themes.xml`
