package org.osate.aadl.evaluator.ui.automaticWizard.select;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.osate.aadl.evaluator.evolution.FuncionalityUtils;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.project.Declaration;
import org.osate.aadl.evaluator.project.Feature;
import org.osate.aadl.evaluator.project.Property;
import org.osate.aadl.evaluator.project.Subcomponent;

public class AutomaticOptionsRunnable implements Runnable
{
    private final static Logger LOGGER = Logger.getLogger(AutomaticOptionsRunnable.class.getName() );
    private final AutomaticSelectJPanel panel;
    
    public AutomaticOptionsRunnable( AutomaticSelectJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void run() 
    {
        LOGGER.info( "Declaration rebuild..." );
        
        panel.setMessage( "Clear the elements..." );
        clear();
        
        panel.setMessage( "Processing..." );
        panel.setRoot( createNode( null , panel.getSystem() ) );
    }
    
    private void clear()
    {
        DefaultTreeTableModel model = panel.getTreeTableModel();
        DefaultMutableTreeTableNode root = (DefaultMutableTreeTableNode) model.getRoot();
        
        for( int i = 0 ; i < root.getChildCount() ; i++ )
        {
            model.removeNodeFromParent( 
                (DefaultMutableTreeTableNode) root.getChildAt( i ) 
            );
        }
    }
    
    private DefaultMutableTreeTableNode createNode( Declaration declaration , Component component )
    {
        DefaultMutableTreeTableNode node = findCandidates( declaration , component );
        
        Map<String,Feature> features = component.getFeaturesAll();
        Map<String,Subcomponent> subcomponents = component.getSubcomponentsAll();
        
        LOGGER.log( Level.INFO , "Create node.....: {0}" , component.getFullName() );
        LOGGER.log( Level.INFO , "........features: {0}" , features.keySet() );
        LOGGER.log( Level.INFO , "...subcomponents: {0}" , subcomponents.keySet() );
        
        if( !features.isEmpty() )
        {
            node.add( createNode( "Features" , features.values() ) );
        }
        
        if( !subcomponents.isEmpty() )
        {
            node.add( createNode( "Subcomponents" , subcomponents.values() ) );
        }
        
        return node;
    }
    
    private DefaultMutableTreeTableNode createNode( String name , Collection<? extends Declaration> declarations )
    {
        DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode( name , true );
        
        for( Declaration declaration : declarations )
        {
            LOGGER.log( Level.WARNING  , "processing: {0} {1}.{2}" , new Object[]{ 
                declaration.getType() ,
                declaration.getParent() , 
                declaration.getName() 
            } );
            
            Component ref = declaration.getComponent();
            
            if( ref == null )
            {
                LOGGER.log( Level.WARNING  , "component not found: {0}" , declaration.getComponentReferenceName() );
                node.add( new DefaultMutableTreeTableNode( declaration ) );
            }
            else
            {
                node.add( createNode( declaration , ref ) );
            }
        }

        return node;
    }
    
    private DefaultMutableTreeTableNode findCandidates( Declaration declaration , Component component )
    {
        if( component == null )
        {
            return new DefaultMutableTreeTableNode( declaration );
        }
        
        Map<String,List<Property>> funcionalities = FuncionalityUtils.list( component );
        
        if( funcionalities.isEmpty() )
        {
            return new DefaultMutableTreeTableNode( component );
        }
        
        return new DefaultMutableTreeTableNode( 
            new AutomaticOptionsFinder( declaration , funcionalities.keySet() ).find()
        );
    }
    
}
