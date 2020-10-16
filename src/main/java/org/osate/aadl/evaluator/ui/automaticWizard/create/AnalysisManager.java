package org.osate.aadl.evaluator.ui.automaticWizard.create;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import org.osate.aadl.aadlevaluator.report.evaluate.BandwidthEvaluate;
import org.osate.aadl.evaluator.automatic.AutomaticEvolution;
import org.osate.aadl.evaluator.project.Component;

public class AnalysisManager 
{
    
    private AnalysisManager()
    {
        // faz nada
    }
    
    public static void analysis( AutomaticEvolution evolution )
    {
        try
        {
            Component system = evolution.getSystemWidthChanges();
            
            bandwidth( evolution , system );
        }
        catch( Exception err )
        {
            evolution.setStatus( AutomaticEvolution.STATUS_IGNORED );
            evolution.getMessages().add( "An error occurred: " + err.getMessage() );
        }
    }
    
    private static void bandwidth( AutomaticEvolution evolution , Component system )
    {
        List<BandwidthEvaluate.BusResult> results = new BandwidthEvaluate( system )
            .evaluate();
        
        for( BandwidthEvaluate.BusResult busResult : results )
        {
            Collection<String> avaliables = busResult.getDeviceCommonAvaliables();
            
            if( avaliables.isEmpty() )
            {
                evolution.setStatus( AutomaticEvolution.STATUS_IGNORED );
                
                evolution.getMessages().add( MessageFormat.format(
                    "The devices connected to bus {0} doesn't have a common bandwidth." , 
                    busResult.getSubcomponent().getName()
                ) );
            }
            else if( !avaliables.contains( busResult.getBandwidth() ) )
            {
                evolution.setStatus( AutomaticEvolution.STATUS_IGNORED );
                
                evolution.getMessages().add( MessageFormat.format(
                    "The bandwidth of the bus {0} is not present in common bandwidth of the devices. We suggest one of these values {1}" , 
                    busResult.getSubcomponent().getName() ,
                    avaliables
                ) );
            }
            
            if( busResult.getResults().size() > busResult.getConnectionMax() )
            {
                evolution.setStatus( AutomaticEvolution.STATUS_IGNORED );
                
                evolution.getMessages().add( MessageFormat.format(
                    "The number of devices connected ({0}) to the bus {1} was more than supported ({2})." , 
                    busResult.getResults().size() ,
                    busResult.getSubcomponent().getName() ,
                    busResult.getConnectionMax()
                ) );
            }
            
            // TODO: é necessário verificar se o consumo do barramento passou o limite dele?
            
            for( BandwidthEvaluate.IndividualResult r : busResult.getResults() )
            {
                for( Object error : r.getErrors() )
                {
                    evolution.getMessages().add( error.toString() );
                }
            }
        }
    }
    
}