package com.jn.web.goods.service.impl;

import com.jn.entity.Result;
import com.jn.pojo.Address;
import com.jn.web.goods.dao.AddressMapper;
import com.jn.web.goods.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author muYan
 * @Version 1.0
 * @Since 2021-02-02
 */
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressMapper addressMapper;

    @Override
    public Result<List<Address>> list(String username) {
        Address address = new Address();
        address.setUsername(username);
        List<Address> addressList = addressMapper.select(address);
        Result<List<Address>> result = new Result<>();
        result.setData(addressList);
        return result;
    }
}
