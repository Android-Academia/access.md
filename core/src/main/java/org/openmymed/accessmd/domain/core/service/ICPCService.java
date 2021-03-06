/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmymed.accessmd.domain.core.service;

import java.util.List;
import org.openmymed.accessmd.domain.core.entity.ICPCEntry;

/**
 *
 * @author tareq
 */
public interface ICPCService {
    
    List<ICPCEntry> getSymptoms();
    List<ICPCEntry> getInfections();
    List<ICPCEntry> getNeoplasms();
    List<ICPCEntry> getInjuries();
    List<ICPCEntry> getProcesses();
    List<ICPCEntry> getCongenitalAnomalies();
    List<ICPCEntry> getEntries();
    List<ICPCEntry> getOther();
    
    void setEntries(List<ICPCEntry> entries);
    
    
}
