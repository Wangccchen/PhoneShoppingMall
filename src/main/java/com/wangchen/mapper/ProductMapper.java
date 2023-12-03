package com.wangchen.mapper;

import com.wangchen.pojo.Product;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    //增加产品
//    @Insert("INSERT INTO Products (ProductName, Description, Price, StockQuantity, SalesVolume, ImageURL) VALUES (#{productName}, #{description}, #{price}, #{stockQuantity}, 0, COALESCE(#{imageURL}, 'https://example.com/default-image.jpg'))")
    void addProduct(Product product);

    //删除产品
//    @Delete("DELETE FROM Products WHERE ProductID = #{productID}")
    void deleteProduct(int productID);

    //搜索产品（实现模糊搜索）
//    @Select("SELECT * FROM Products WHERE ProductName LIKE CONCAT('%', #{keyword}, '%')")
    List<Product> searchProductsByName(@Param("keyword") String keyword);

    //根据id获取产品
//    @Select("SELECT * FROM Products WHERE ProductID = #{productID}")
    Product getProductById(Integer productID);

    //显示全部商品
//    @Select("SELECT * FROM Products")
    List<Product> getAllProducts();

    //更新产品参数
//    @Update("UPDATE Products SET ProductName = #{productName}, Description = #{description}, Price = #{price}, StockQuantity = #{stockQuantity} WHERE ProductID = #{productID}")
    void updateProduct(Product product);

    void deleteProductsByIds(List<Integer> productIds);

    //分页
    List<Product> getProductsByPage(@Param("start") int start, @Param("pageSize") int pageSize);

    int getTotalProductsCount();

    List<Product> getProductsOrderBySalesVolume();

    List<Product> getProductsByProductIds(List<Integer> productIds);


}

