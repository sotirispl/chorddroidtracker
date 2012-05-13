package gr.aueb.mscis.tracker;

import java.net.InetAddress;

/**
 * This class represents a record of a Node that have requested 
 * the Tracker to join the chord 
 * @author lupin
 *
 */
public class TrackerRecord {
        private InetAddress addr;
        private Integer port;
       
        public TrackerRecord(InetAddress addr, Integer port) {
                super();
                this.addr = addr;
                this.port = port;
        }
        
        public TrackerRecord(InetAddress addr) {
                super();
                this.addr = addr;
                this.port = 5001;
        }

        public InetAddress getAddr() {
                return addr;
        }

        public void setAddr(InetAddress addr) {
                this.addr = addr;
        }

        public Integer getPort() {
                return port;
        }

        public void setPort(Integer port) {
                this.port = port;
        }
        
        @Override
        public String toString() {
                return new String("chrodroid://" + getAddr().getHostAddress() + ":" + getPort());
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + ((addr == null) ? 0 : addr.hashCode());
                result = prime * result + ((port == null) ? 0 : port.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                TrackerRecord other = (TrackerRecord) obj;
                if (addr == null) {
                        if (other.addr != null)
                                return false;
                } else if (!addr.equals(other.addr))
                        return false;
                if (port == null) {
                        if (other.port != null)
                                return false;
                } else if (!port.equals(other.port))
                        return false;
                return true;
        }


}
