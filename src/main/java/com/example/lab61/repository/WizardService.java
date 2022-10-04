package com.example.lab61.repository;

import com.example.lab61.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public WizardService(WizardRepository repository){
        this.repository = repository;
    }

    public List<Wizard> retrieveWizards(){
        return repository.findAll();
    }

    public Wizard createWizard(Wizard wizard){
        return repository.save(wizard);
    }
    public Wizard updateWizard(Wizard wizard){
        return repository.save(wizard);
    }
    public Wizard retrieveById(String _id){

        return repository.findByID(_id);
    }

    public boolean deleteWizard(Wizard wizard){
        try{repository.delete(wizard); return true;}
        catch (Exception e) { return  false;}
    }
}
