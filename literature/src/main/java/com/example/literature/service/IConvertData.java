package com.example.literature.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> tClass);
}
