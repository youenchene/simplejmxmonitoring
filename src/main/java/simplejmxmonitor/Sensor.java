package simplejmxmonitor;

public class Sensor {
   private Long id;
   private String name;
   private String ip;
   private Integer jmxport;
   private String status;
   private String statusMbean;
    private String statusMbeanUp;

    public Sensor(Long id, String name, String ip, Integer jmxport, String status, String statusMbean, String statusMbeanUp) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.jmxport = jmxport;
        this.status = status;
        this.statusMbean = statusMbean;
        this.statusMbeanUp = statusMbeanUp;
    }

    public String getStatusMbeanUp() {
        return statusMbeanUp;
    }

    public void setStatusMbeanUp(String statusMbeanUp) {
        this.statusMbeanUp = statusMbeanUp;
    }

    public Sensor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getJmxport() {
        return jmxport;
    }

    public void setJmxport(Integer jmxport) {
        this.jmxport = jmxport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusMbean() {
        return statusMbean;
    }

    public void setStatusMbean(String statusMbean) {
        this.statusMbean = statusMbean;
    }

}