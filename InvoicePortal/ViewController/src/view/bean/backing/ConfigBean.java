package view.bean.backing;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import model.pojo.ViewObjAttribute;

import oracle.adf.model.BindingContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;


public class ConfigBean {
    public ConfigBean() {
        super();
    }
    
    public List getShuttleList(){
        BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBind = bindings.getOperationBinding("getViewObjectAttributes");
        List compAttr = (List)operationBind.execute();  
        
        List shuttleList = new ArrayList();
        for(int i =0; i < compAttr.size(); i++){
            ViewObjAttribute attr = (ViewObjAttribute)compAttr.get(i);
            SelectItem item = new SelectItem(i, attr.getColumnName(), attr.getColumnAliasName());
            shuttleList.add(item);
        }
        return shuttleList;
            
        }

    public void createViewActionListener(ActionEvent actionEvent) {
        
    }
}
