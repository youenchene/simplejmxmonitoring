package simplejmxmonitor;

import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.annotation.CsvFile;

@CsvFile
public class SensorCsv {

   @CsvColumn("name")
   public String name;

   @CsvColumn("ip")
   public String ip;

   @CsvColumn("jmxport")
   public String jmxport;

   @CsvColumn("statusMbean")
   public String statusMbean;

   @CsvColumn("statusMbeanUp")
   public String statusMbeanUp;


}
