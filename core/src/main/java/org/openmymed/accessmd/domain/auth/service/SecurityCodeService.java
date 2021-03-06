/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmymed.accessmd.domain.auth.service;

import org.openmymed.accessmd.domain.auth.entity.SecurityCode;

/**
 *
 * @author tareq
 */
public interface SecurityCodeService {
    
    public SecurityCode createSecurityCode(long userId);
    
    public long consumeSecurityCode(String code, long consumerId);
    
    public void expireCodes();
}
