package com.example.lab61.controller;

import com.example.lab61.pojo.Wizard;
import com.example.lab61.pojo.Wizards;
import com.example.lab61.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;
    protected Wizards wizards = new Wizards();

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizards(){
        List<Wizard> wizard = wizardService.retrieveWizards();
        return ResponseEntity.ok(wizard);
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public String addWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard w = wizardService.createWizard(new Wizard(null, d.get("sex"), d.get("name"), d.get("school"), d.get("house"), Integer.parseInt(d.get("money")), d.get("position")));
        return "Wizard has been created.";
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public String updateWizard(@RequestBody Wizard wizard){

        Wizard oldwizard = wizardService.retrieveById(wizard.get_id());
        if(oldwizard != null) {
            wizardService.updateWizard(wizard);
            return "Wizard has been updated.";
        }
        else {
            return "Update failed.";
        }

    }
     @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public String deleteWizard(@RequestBody Wizard wizard){
         boolean status = wizardService.deleteWizard(wizard);
         if(status){
             return "Wizard has been deleted.";
         }
         else {
             return "Delete failed";
         }

     }

}
