package org.osate.aadl.evaluator.ui.automaticWizard;

import org.osate.aadl.evaluator.ui.automaticWizard.create.AutomaticEvolutionListJPanel;
import fluent.gui.impl.swing.FluentWizardPanel;
import fluent.gui.impl.swing.wizard.PaginaPanel;
import fluent.gui.impl.swing.wizard.WizardAdapter;
import java.awt.BorderLayout;
import java.awt.Window;
import java.util.Collection;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.project.Component;
import org.osate.aadl.evaluator.ui.automaticWizard.binding.AutomaticBindingListJPanel;
import org.osate.aadl.evaluator.ui.automaticWizard.select.AutomaticSelectJPanel;

public class AutomaticWizardJDialog extends JDialog
{
    private FluentWizardPanel wizard;
    private AutomaticSelectJPanel selectJPanel;
    private AutomaticBindingListJPanel bindingsJPanel;
    private AutomaticEvolutionListJPanel evolutionJPanel;
    
    private Component system;
    private boolean saved;
    
    public AutomaticWizardJDialog( Window parent )
    {
        super( parent );
        
        init();
        
        setTitle( "Automatic Evolution Wizard" );
        setSize( 800 , 600 );
        setModal( true );
        setLocationRelativeTo( parent );
    }
    
    private void init()
    {
        setLayout( new BorderLayout() );
        saved = false;
        
        add( wizard = new FluentWizardPanel( "Automatic Evolution" ) {
            @Override
            public void fechar() {
                setVisible( false );
            }
        } , BorderLayout.CENTER );
        
        wizard.setActionNames( 
            "Cancel" , 
            "Back" , 
            "Next" , 
            "Finish" 
        );
        
        wizard.addListener(new WizardAdapter(){
            @Override
            public boolean canChangePage(int currentPageIndex, int nextPageIndex) {
                try
                {
                    if( currentPageIndex == 0 )
                    {
                        bindingsJPanel.setOptions( selectJPanel.getSelected() );
                    }
                    else if( currentPageIndex == 1 && nextPageIndex == 2 )
                    {
                        if( bindingsJPanel.getTreeTable().getRowCount() == 0 )
                        {
                            return false;
                        }
                        
                        evolutionJPanel.setOptions( bindingsJPanel.getOptionsFromView() );
                    }
                    
                    return true;
                }
                catch( Exception err )
                {
                    err.printStackTrace();
                    
                    JOptionPane.showMessageDialog( 
                        rootPane , 
                        err.getMessage() , 
                        "Error" , 
                        JOptionPane.ERROR_MESSAGE 
                    );
                    
                    return false;
                }
            }

            @Override
            public void finish( Collection<PaginaPanel> pages ) {
                saved = true;
                setVisible( false );
            }
        });
        
        addPages();
    }
    
    private void addPages()
    {
        wizard.getPaginas().add( new PaginaPanel( 
            selectJPanel = new AutomaticSelectJPanel() , 
                "Please, select one or more components to change it."
            )
        );
        
        wizard.getPaginas().add( new PaginaPanel( 
            bindingsJPanel = new AutomaticBindingListJPanel() , 
                "Please, fix the interfaces & connections."
            )
        );
        
        wizard.getPaginas().add( new PaginaPanel( 
            evolutionJPanel = new AutomaticEvolutionListJPanel() , 
                "Please, all evolution created based on components selected."
            )
        );
        
        wizard.setPaginaAtual( -1 );
        wizard.proxima();
    }
    
    // ---------------------------- //
    // ---------------------------- //
    // ---------------------------- //
    
    public void setSystem( Component system )
    {
        this.system = system.clone();
        this.selectJPanel.setSystem( this.system );
    }

    public Component getSystem()
    {
        return system;
    }
    
    public boolean isSaved()
    {
        return saved;
    }
    
    public List<AutomaticEvolution> getEvolutions()
    {
        return evolutionJPanel.getTable().getTabelModel().getData();
    }
    
}