# Service package

A service is an interface to an external API.
E.g. 

    public interface NotificationService extends Service {

        void setSender(String sender);
    
        void setSubject(String subject);
    
        void setTextMessage(String textMessage);
    
        String sendGreetings(Employee employee);
    
    }
