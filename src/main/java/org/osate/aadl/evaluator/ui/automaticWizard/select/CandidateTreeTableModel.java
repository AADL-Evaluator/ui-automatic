package org.osate.aadl.evaluator.ui.automaticWizard.select;

import java.util.LinkedList;
import java.util.List;
import org.osate.aadl.evaluator.automatic.AutomaticOptions;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

public class CandidateTreeTableModel extends DefaultTreeTableModel
{
    private final String[] columns;
    
    public CandidateTreeTableModel() 
    {
        columns = new String[]{ 
            "Component" , 
            "Candidates" ,
            "selected"
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
    public Class<?> getColumnClass( int column )
    {
        return column == columns.length - 1
            ? Boolean.class 
            : String.class;
    }
    
    @Override
    public boolean isCellEditable( Object node , int column )
    {
        if( !(node instanceof DefaultMutableTreeTableNode) )
        {
            return false;
        }
        
        DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) node;
        
        if( !(n.getUserObject() instanceof AutomaticOptions) )
        {
            return false;
        }
        
        return column == columns.length - 1;
    }
    
    @Override
    public Object getValueAt( final Object node , final int index )
    {
        if( !(node instanceof DefaultMutableTreeTableNode) )
        {
            return "!!!";
        }
        
        DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) node;
        
        if( n.getUserObject() instanceof AutomaticOptions )
        {
            AutomaticOptions value = (AutomaticOptions) n.getUserObject();
            
            switch( index )
            {
                case 0 : return value.getDeclaration();
                case 1 : return value.getComponents().size();
                case 2 : return value.isSelected();
                default: return "";
            }
        }
        else
        {
            switch( index )
            {
                case 0 : return n;
                case 3 : return false;
                default: return "";
            }
        }
    }

    @Override
    public void setValueAt( Object value , Object node , int column )
    {
        if( !(node instanceof DefaultMutableTreeTableNode) )
        {
            return ;
        }
        
        DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) node;
        
        if( n.getUserObject() instanceof AutomaticOptions )
        {
            AutomaticOptions ap = (AutomaticOptions) n.getUserObject();
            ap.setSelected( !ap.isSelected() );
        }
    }
    
    public List<AutomaticOptions> getSelected()
    {
        List<AutomaticOptions> data = new LinkedList<>();
        
        getSelected( (DefaultMutableTreeTableNode) getRoot() , data );
        
        return data;
    }
    
    public void getSelected( DefaultMutableTreeTableNode node , List<AutomaticOptions> options )
    {
        if( node.getUserObject() instanceof AutomaticOptions )
        {
            if( ((AutomaticOptions) node.getUserObject()).isSelected() )
            {
                options.add( (AutomaticOptions) node.getUserObject() );
            }
        }
        
        for( int i = 0 ; i < node.getChildCount() ; i++ )
        {
            getSelected( (DefaultMutableTreeTableNode) node.getChildAt( i ) , options );
        }
    }
    
}
