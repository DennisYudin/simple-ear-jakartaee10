package com.packt.cookbook.ejb.service;


import com.packt.cookbook.ejb.model.Member;

import jakarta.ejb.Local;

@Local
public interface Registration {

	void register(Member member) throws Exception;
}