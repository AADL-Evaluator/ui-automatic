package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.osate.aadl.evaluator.evolution.BindingUtils;
import static org.osate.aadl.evaluator.evolution.BindingUtils.getFeatureName;
import static org.osate.aadl.evaluator.evolution.BindingUtils.getSubcomponentName;
import org.osate.aadl.evaluator.evolution.BusUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Subcomponent;
import org.osate.aadl.evaluator.automatic.AutomaticBinding;

public class DetailsRunnable implements Runnable
{
    private final AutomaticBindingListJPanel panel;
    private final JTextArea jTextArea;
    private final Set<Object> added;

    public DetailsRunnable( AutomaticBindingListJPanel panel , JTextArea jTextArea )
    {
        this.panel = panel;
        this.jTextArea = jTextArea;
        this.added = new HashSet<>();
    }

    @Override
    public void run() 
    {
        jTextArea.setText( "" );
        
        if( panel.getTreeTable().getSelectedRow() == -1 )
        {
            return ;
        }
        
        int[] rows = panel.getTreeTable().getSelectedRows();
        
        for( int row : rows )
        {
            TreePath path = panel.getTreeTable().getPathForRow( row );
            setData( "" , (DefaultMutableTreeTableNode) path.getLastPathComponent() );
        }
        
        this.added.clear();
    }
    
    public void setData( String space , DefaultMutableTreeTableNode node )
    {
        // it is to avoid print the same data twice or more
        if( added.contains( node ) )
        {
            return ;
        }
        
        added.add( node );
        
        // ---
        
        if( node.getChildCount() == 0 )
        {
            if( node.getUserObject() instanceof AutomaticBinding )
            {
                AutomaticBinding ab = (AutomaticBinding) node.getUserObject();
                setData( space , ab.getSystem() , ab );
            }
        }
        else
        {
            if( !jTextArea.getText().isEmpty() )
            {
                append( space );
                append( "\n\n" );
            }
            
            append( space );
            append( node.getUserObject().toString() );
                
            for( int i = 0 ; i < node.getChildCount() ; i++ )
            {
                setData( space + "   " , (DefaultMutableTreeTableNode) node.getChildAt( i ) );
            }
        }
    }
    
    public void setData( String space , Component system , AutomaticBinding binding )
    {
        if( !jTextArea.getText().isEmpty() )
        {
            append( space );
            append( "\n" );
        }
        
        try
        {
            if( binding.getConnection() != null )
            {
                append( space );
                append( binding.getConnection().toString() );
                append( "\n" );
            }
            
            append( space );
            append( "Candidate: " , 11 );
            append( binding.getPartA() , 45 );
            append( getValue(system , binding.getPartA() ) + "\n" );
            
            append( space );
            append( "Soft/hard: " );
            append( binding.getPartB() , 45 );
            append( getValue(system , binding.getPartB() ) + "\n" );
            
            append( space );
            append( "BUS & CPU: " );
            append( BusUtils.toString( binding.getBusesAndCpus() ) );
            append( "\n"  );
            
            append( space );
            append( "Status: " , 11 );
            append( BindingUtils.getCompatibleMessage( system , binding ) );
        }
        catch( Exception err )
        {
            append( space );
            append( "Status: " , 11 );
            append( "Error to valid it" , 30 );
            append( " (reason: " + err.getMessage() + ")" );
        }
        
        append( "\n" );
    }
    
    public String getValue( Component system , String part )
    {
        String subName = getSubcomponentName( part );
        String feaName = getFeatureName( part );
        
        if( subName == null )
        {
            return "The binding link to nobody!";
        }
        
        Subcomponent subcomponent = system.getSubcomponent( subName );
        
        if( subcomponent == null )
        {
            return "subcomponent " + subName + " not found!";
        }
        
        String value = system.getSubcomponent( subName ).getValue();
        
        if( feaName != null )
        {
            Component c = subcomponent.getComponent();
            
            if( c == null )
            {
                return "component " + subcomponent.getComponentReferenceName() + " not found!";
            }
            
            value = c.getFeature( feaName ).getValue();
        }
        
        return value;
    }
    
    private void append( String value )
    {
        jTextArea.append( value );
    }
    
    private void append( String value , int size )
    {
        for( int i = 0 ; i < size ; i++ )
        {
            jTextArea.append( 
                i < value.length() ? value.charAt( i ) + "" : " "
            );
        }
    }
    
}