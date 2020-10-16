package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;

public class ChangeStatusActionListener implements ActionListener
{
    private final AutomaticEvolutionListJPanel panel;

    public ChangeStatusActionListener( AutomaticEvolutionListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        int status = getStatus();
        if( status == -1 )
        {
            return ;
        }
        
        final ChangeStatusJDialog dialog = new ChangeStatusJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        dialog.setStatus( status );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( dialog.isSaved() )
        {
            setStatus( dialog.getStatus() );
        }
    }
    
    private int getStatus()
    {
        if( panel.getTable().getSelectedRow() == -1 )
        {
            return -1;
        }
        
        return panel.getTable().getSelectedObject().getStatus();
    }
    
    private void setStatus( int status )
    {
        for( AutomaticEvolution evolution : panel.getTable().getSelectedObjects() )
        {
            evolution.setStatus( status );
        }
        
        panel.reorder();
    }
    
}
