package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.awt.BorderLayout;
import java.awt.Window;
import java.util.List;
import javax.swing.JDialog;
import org.jdesktop.swingx.JXHeader;
import org.osate.aadl.evaluator.evolution.Binding;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.ui.secondWizard.binding.BindingListJPanel;

public class BindingListJDialog extends JDialog
{
    private AutomaticEvolution evolution;
    private BindingListJPanel panel;
    
    public BindingListJDialog( Window parent )
    {
        super( parent );
        
        init();
        
        setTitle( "Interfaces & Connections" );
        setSize( 600 , 500 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        setLayout( new BorderLayout() );
        
        add( new JXHeader( 
            "Interfaces & Connections" , 
            "This window show all interfaces & connections could be created in this evolution." 
        ) , BorderLayout.NORTH );
        
        add( panel = new BindingListJPanel() , BorderLayout.CENTER );
    }

    public void setEvolution( AutomaticEvolution evolution )
    {
        this.evolution = evolution;
        this.panel.setEvolution( evolution );
    }
    
    public AutomaticEvolution getEvolution() 
    {
        return evolution;
    }
    
    public List<Binding> getBindings() 
    {
        return panel.getTable().getTabelModel().getData();
    }
    
}

