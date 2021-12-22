package com.jn.web.goods.service;

import com.jn.entity.Result;
import com.jn.pojo.Address;

import java.util.List;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-02
 */
public interface AddressService {

    Result<List<Address>> list(String username);
}
