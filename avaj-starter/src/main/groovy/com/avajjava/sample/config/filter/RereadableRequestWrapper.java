//import org.apache.commons.lang.StringUtils;
//
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.nio.charset.Charset;
//import java.util.*;
//
//public class RereadableRequestWrapper extends HttpServletRequestWrapper {
//
//    private boolean parametersParsed = false;
//
//    private final Charset encoding = Charset.defaultCharset();
//    private final byte[] rawData = new byte[1];
//    private final Map<String, ArrayList<String>> parameters = new LinkedHashMap<String, ArrayList<String>>();
//    ByteChunk tmpName = new ByteChunk();
//    ByteChunk tmpValue = new ByteChunk();
//
//    private class ByteChunk {
//
//        private byte[] buff;
//        private int start = 0;
//        private int end;
//
//        public void setByteChunk(byte[] b, int off, int len) {
//            buff = b;
//            start = off;
//            end = start + len;
//        }
//
//        public byte[] getBytes() {
//            return buff;
//        }
//
//        public int getStart() {
//            return start;
//        }
//
//        public int getEnd() {
//            return end;
//        }
//
//        public void recycle() {
//            buff = null;
//            start = 0;
//            end = 0;
//        }
//    }
//
//
//
//
//
//    @Override
//    public String getParameter(String name) {
//        if (!parametersParsed) {
//            parseParameters();
//        }
//        ArrayList<String> values = this.parameters.get(name);
//        if (values == null || values.size() == 0)
//            return null;
//        return values.get(0);
//    }
//
//    public HashMap<String, String[]> getParameters() {
//        if (!parametersParsed) {
//            parseParameters();
//        }
//        HashMap<String, String[]> map = new HashMap<String, String[]>(this.parameters.size() * 2);
//        for (String name : this.parameters.keySet()) {
//            ArrayList<String> values = this.parameters.get(name);
//            map.put(name, values.toArray(new String[values.size()]));
//        }
//        return map;
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    public Map getParameterMap() {
//        return getParameters();
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    public Enumeration getParameterNames() {
//        return new Enumeration<String>() {
//            @SuppressWarnings("unchecked")
//            private String[] arr = (String[])(getParameterMap().keySet().toArray(new String[0]));
//            private int index = 0;
//
//            @Override
//            public boolean hasMoreElements() {
//                return index < arr.length;
//            }
//
//            @Override
//            public String nextElement() {
//                return arr[index++];
//            }
//        };
//    }
//
//    @Override
//    public String[] getParameterValues(String name) {
//        if (!parametersParsed) {
//            parseParameters();
//        }
//        ArrayList<String> values = this.parameters.get(name);
//        String[] arr = values.toArray(new String[values.size()]);
//        if (arr == null) {
//            return null;
//        }
//        return arr;
//    }
//
//    private void parseParameters() {
//        parametersParsed = true;
//
//        if (!("application/x-www-form-urlencoded".equalsIgnoreCase(super.getContentType()))) {
//            return;
//        }
//
//        int pos = 0;
//        int end = this.rawData.length;
//
//        while (pos < end) {
//            int nameStart = pos;
//            int nameEnd = -1;
//            int valueStart = -1;
//            int valueEnd = -1;
//
//            boolean parsingName = true;
//            boolean decodeName = false;
//            boolean decodeValue = false;
//            boolean parameterComplete = false;
//
//            do {
//                switch (this.rawData[pos]) {
//                    case '=':
//                        if (parsingName) {
//                            // Name finished. Value starts from next character
//                            nameEnd = pos;
//                            parsingName = false;
//                            valueStart = ++pos;
//                        } else {
//                            // Equals character in value
//                            pos++;
//                        }
//                        break;
//                    case '&':
//                        if (parsingName) {
//                            // Name finished. No value.
//                            nameEnd = pos;
//                        } else {
//                            // Value finished
//                            valueEnd = pos;
//                        }
//                        parameterComplete = true;
//                        pos++;
//                        break;
//                }
//
//                if (StringUtils.isNotBlank(name)) {
//                    ArrayList<String> values = this.parameters.get(name);
//                    if (values == null) {
//                        values = new ArrayList<String>(1);
//                        this.parameters.put(name, values);
//                    }
//                    if (StringUtils.isNotBlank(value)) {
//                        values.add(value);
//                    }
//                }
//            } catch (DecoderException e) {
//                // ignore invalid chunk
//            }
//
//            tmpName.recycle();
//            tmpValue.recycle();
//        }
//    }
//}
