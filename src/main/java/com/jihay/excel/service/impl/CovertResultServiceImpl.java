package com.jihay.excel.service.impl;

import com.jihay.excel.entity.CovertResult;
import com.jihay.excel.repo.CovertResultRepo;
import com.jihay.excel.service.CovertResultService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CovertResultServiceImpl implements CovertResultService {

    @Resource
    private CovertResultRepo covertResultRepo;

    public List<CovertResult> query(CovertResult covertResult){

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("specifications", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("material" ,ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CovertResult> example = Example.of(covertResult, matcher);

        Sort.Order order = Sort.Order.asc("price");
        Sort sort = Sort.by(order);

        return covertResultRepo.findAll(example, sort);
    }
}
