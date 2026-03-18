package com.urlshortner.repository;

import com.urlshortner.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UrlRepository {

    public void save(String shortCode, String longUrl) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO urls(short_code, long_url) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, shortCode);
            stmt.setString(2, longUrl);

            stmt.executeUpdate();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String shortCode) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT long_url FROM urls WHERE short_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, shortCode);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("long_url");
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void incrementClicks(String shortCode) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE urls SET click_count = click_count + 1 WHERE short_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
    
            stmt.setString(1, shortCode);
            stmt.executeUpdate();
    
            conn.close();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}