package org.osate.aadl.evaluator.ui.automaticWizard;

import java.util.List;
import javax.swing.SwingUtilities;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.ui.mainWizard.AadlUIAction;
import org.osate.aadl.evaluator.ui.p3.EvolutionListJPanel;

public class AutomaticAadlUIAction extends AadlUIAction
{

    public AutomaticAadlUIAction()
    {
        super( "Automatic changes creator" );
    }
    
    @Override
    public List<AutomaticEvolution> execute( final EvolutionListJPanel panel ) throws Exception
    {
        final AutomaticWizardJDialog dialog = new AutomaticWizardJDialog(
            SwingUtilities.getWindowAncestor( panel )
        );
        
        dialog.setSystem( panel.getSystemOriginal() );
        dialog.setVisible( true );
        dialog.dispose();
        
        if( !dialog.isSaved() )
        {
            throw new Exception( "You canceled the operation." );
        }
        
        return dialog.getEvolutions();
    }
    
}
