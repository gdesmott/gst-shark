package com.ridgerun.gstshark;



import java.util.ArrayList;
import java.util.List;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.timestamp.ITmfTimestamp;
import org.eclipse.tracecompass.tmf.core.trace.ITmfContext;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfCommonXLineChartViewer;

public class GstSharkBaseViewer extends TmfCommonXLineChartViewer {

	private ITmfTrace _trace = null;
	private String _event = null;
	//private static final String FIELD_VALUE = "fakesink0_sink";
	private static final String FIELD_VALUE = "queue0_sink";
	
	public GstSharkBaseViewer(Composite parent, String title, String xLabel, String yLabel, String event) {
		super(parent, title, xLabel, yLabel);
		_event = event;
	}
	
	private ITmfEvent getNextFilteredByName(ITmfContext ctx, String name) {
		ITmfEvent event = null;
		String eventName;
		
		do {
			event = _trace.getNext(ctx);
			// Avoid while verification if there is not a next event in the list 
			if (event == null)
			{
				return null;
			}
			eventName = event.getName().toString();
			//System.out.println(String.format("Event name %s",eventName));
			
			// Avoid while verification if the event is init because this event doesn't have a field
			if (0 == eventName.compareTo("init"))
			{
				continue;
			}
			
	  } while (null == event || !event.getName().equals(name) || !event.getContent().getField("elementname").getValue().equals(FIELD_VALUE));
	  //} while (null == event || !event.getName().equals(name) || !event.getContent().getField("elementname").getValue().equals("identity0_sink"));

		return event;
	}
	
	private List<ITmfEvent> getEventsInRange (long start, long end) {
		List<ITmfEvent> events = new ArrayList<ITmfEvent>();
		ITmfTimestamp startTimestamp = _trace.createTimestamp(start);
		ITmfTimestamp endTimestamp = _trace.createTimestamp(end);
		System.out.println(String.format("Range is %s to %s", startTimestamp.toString(), endTimestamp.toString()));
		ITmfContext ctx = _trace.seekEvent(startTimestamp);
		ITmfEvent event = getNextFilteredByName(ctx, _event);

		while (null != event && 0 > event.getTimestamp().compareTo(endTimestamp)) {
			events.add(event);
			event = getNextFilteredByName(ctx, _event);
		}
		if (events.size() > 0)
		{
		  System.out.println(String.format("Last ts %s - end %s", events.get(events.size()-1).getTimestamp(), endTimestamp));
		}
		
		return events;
	}
	
	
	
	@Override
	protected void updateData(long start, long end, int nb, IProgressMonitor monitor) {
		System.out.print(String.format("Starting time: %d\n", getStartTime()));
		System.out.print(String.format("End time: %d\n", getEndTime()));
		System.out.print(String.format("W Start time: %d\n", getWindowStartTime()));
		System.out.print(String.format("W end time: %d\n", getWindowEndTime()));
		System.out.print(String.format("S start time: %d\n", getSelectionBeginTime()));
		System.out.print(String.format("S end time: %d\n", getSelectionEndTime()));

		System.out.println(start);
		System.out.println(end);
		System.out.println(nb);
		System.out.println(getTimeOffset());
			
		List <ITmfEvent> events = getEventsInRange(start, end);
		
		if (events.size() == 0)
		{
			System.out.println("**** Event Size = 0");
			//updateDisplay();
			return;
		}
		
		//events = resizeEventList (events, nb);
		double xx[] = new double[events.size()];
		double y[] = new double[events.size()];
		System.out.println("===========================");
		for (int i = 0; i < events.size(); ++i) {
			xx[i] = events.get(i).getTimestamp().getValue() - getTimeOffset();
			y[i] = new Double(events.get(i).getContent().getField("time").getValue().toString());

			//System.out.print(String.format("%f - %f\n", x[i], y[i]));
		}
		double x[] = getXAxis(start, end, events.size());
		System.out.println(String.format("%f - %f - %f", x[0], xx[0], y[0]));
		//System.out.println(String.format("%f - %f - %f", x[x.length-1], xx[x.length-1], y[x.length-1]));

		System.out.println("===========================");

		//clearContent();
		setXAxis(xx);
		setSeries(FIELD_VALUE, y);
		updateDisplay();
	}	

	@Override
	public void loadTrace(ITmfTrace trace) {
		super.loadTrace(trace);
		_trace = trace;
	}
	
	@Override
	protected void createSeries() {
		ITmfContext context = _trace.seekEvent(0);
		ITmfEvent event = _trace.getNext(context);
		while (event != null) {
			if ("scheduling".equals(event.getName())) {
				
				
				break;
			}
			event = _trace.getNext(context);
		}
		//addSeries("test1");
    }
}