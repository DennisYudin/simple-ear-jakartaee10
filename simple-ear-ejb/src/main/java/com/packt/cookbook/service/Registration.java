package com.packt.cookbook.service;


import com.packt.cookbook.model.Member;

import jakarta.ejb.Local;

@Local
public interface Registration {

	void register(Member member) throws Exception;
}