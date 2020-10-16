package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;

public class OptionsTreeTableModel extends DefaultTreeTableModel
{
    private final String[] columns;
    
    public OptionsTreeTableModel() 
    {
        columns = new String[]{ 
            "Component" , 
            "Message"
        };
    }
    
    @Override
    public int getColumnCount() 
    {
        return columns.length;
    }
    
    @Override
    public String getColumnName( int index ) 
    {
        return columns[ index ];
    }
    
    @Override
    public boolean isCellEditable( Object node , int column )
    {
        return false;
    }
    
    @Override
    public Object getValueAt( final Object node , final int index )
    {
        if( !(node instanceof DefaultMutableTreeTableNode) )
        {
            return "!!!";
        }
        
        DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) node;
        
        if( n.getUserObject() instanceof AutomaticBinding )
        {
            AutomaticBinding binding = (AutomaticBinding) n.getUserObject();
            
            switch( index )
            {
                case 0 : return binding.toString();
                case 1 : return BindingUtils.getCompatibleMessage( binding );
                default: return "";
            }
        }
        else
        {
            switch( index )
            {
                case 0 : return n.getUserObject();
                default: return "";
            }
        }
    }
    
}
