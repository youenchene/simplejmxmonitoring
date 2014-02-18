package simplejmxmonitor;

import fr.ybonnel.csvengine.CsvEngine;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.model.InsertObject;
import fr.ybonnel.simpleweb4j.handlers.resource.RestResource;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class SensorResource extends RestResource<Sensor> {

    protected static Map<Long, Sensor> entities = new HashMap<>();



    public SensorResource(String route) {
        super(route, Sensor.class);

        CsvEngine engine = new CsvEngine(SensorCsv.class);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/etc/simplejmxmonitor/conf.csv");
            try {
                engine.parseFileAndInsert(new InputStreamReader(fis), SensorCsv.class, new InsertObject<SensorCsv>() {
                    @Override
                    public void insertObject(SensorCsv sensorCsv) {
                        Long id=entities.size()+1l;
                        entities.put(id,new Sensor(id,sensorCsv.name,sensorCsv.ip,Integer.parseInt(sensorCsv.jmxport),"UNKNOWN",sensorCsv.statusMbean,sensorCsv.statusMbeanUp));
                    }
                });
            } catch (CsvErrorsExceededException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            finally {
                fis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Override
    public Sensor getById(String id) {
        return entities.get(Long.parseLong(id));
    }

    @Override
    public Collection<Sensor> getAll() {

        Iterator<Long> it=entities.keySet().iterator();
        while(it.hasNext())
        {
            Long id=it.next();
            try {
                Sensor sensor=entities.get(id);
                String url = "service:jmx:rmi:///jndi/rmi://"+sensor.getIp()+":"+sensor.getJmxport()+"/jmxrmi";
                JMXServiceURL serviceUrl = new JMXServiceURL(url);
                JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, null);
                try {
                    MBeanServerConnection mbeanConn = jmxConnector.getMBeanServerConnection();
                    // now query to get the beans or whatever
                    if ((sensor.getStatusMbean()!=null)&&(!sensor.getStatusMbean().equals("")))
                    {
                        ObjectName mBean = new ObjectName (sensor.getStatusMbean());


                        String status = "";
                        if (sensor.getStatusMbeanUp().equals("Started"))
                        {
                            status=mbeanConn.getAttribute(mBean,"State").toString();
                        }
                        if (sensor.getStatusMbeanUp().equals("true"))
                        {
                            status=mbeanConn.getAttribute(mBean,"Started").toString();
                        }
                        if (sensor.getStatusMbeanUp().equals("0"))
                        {
                            status=mbeanConn.getAttribute(mBean,"CountWaitingData").toString();
                            if (Integer.parseInt(status)>=0)
                                status="0";
                        }

                        if (status.equals(sensor.getStatusMbeanUp()))
                            sensor.setStatus("UP");
                        else
                            sensor.setStatus("DOWN");
                    }

                } catch (MalformedObjectNameException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (AttributeNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (MBeanException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ReflectionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (InstanceNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } finally {
                    jmxConnector.close();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }



        return entities.values();
    }

    @Override
    public void update(String id, Sensor resource) {
        Long identity=Long.parseLong(id);
       entities.put(identity,resource);
    }

    @Override
    public Sensor create(Sensor resource) {
        long maxId = 0;
        for (Sensor sensor : getAll()) {
            if (sensor.getId() > maxId) {
                maxId = sensor.getId();
            }
        }
        maxId++;
        resource.setId(maxId);
        entities.put(maxId, resource);
        return resource;
    }

    @Override
    public void delete(String id) {
        entities.remove(Long.parseLong(id));
    }

}