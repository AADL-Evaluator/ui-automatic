package org.osate.aadl.evaluator.ui.automaticWizard.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class DetailsActionListener implements ActionListener
{
    private final AutomaticBindingListJPanel panel;

    public DetailsActionListener( AutomaticBindingListJPanel panel )
    {
        this.panel = panel;
    }
    
    @Override
    public void actionPerformed( ActionEvent e )
    {
        if( panel.getTreeTable().getSelectedRow() == -1 )
        {
            return ;
        }
        
        DetailsJDialog dialog = new DetailsJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        // --- //
        
        DetailsRunnable runnable = new DetailsRunnable( 
            panel , 
            dialog.getViewJTextArea() 
        );
        runnable.run();
        
        // --- //
        
        dialog.setVisible( true );
        dialog.dispose();
    }
    
}