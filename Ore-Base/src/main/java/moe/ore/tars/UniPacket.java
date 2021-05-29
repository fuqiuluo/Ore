package moe.ore.tars;

import moe.ore.helper.bytes.ByteArrayExtKt;
import moe.ore.util.bytes.ByteBuilder;
import moe.ore.protocol.tars.RequestPacket;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class UniPacket {
    static HashMap<String, byte[]> newCache__tempdata = null;
    static HashMap<String, HashMap<String, byte[]>> cache__tempdata = null;

    private int version = 3;

    private int requestId = 0;
    private String servantName = "";
    private String funcName = "";

    private final RequestPacket _package = new RequestPacket();
    private HashMap<String, byte[]> _newData = new HashMap<>();
    private HashMap<String, HashMap<String, byte[]>> _data = new HashMap<>();

    public <T> void put(String mapName, T t) {
        if (mapName == null) {
            throw new IllegalArgumentException("put key can not is null");
        } else if (t == null) {
            throw new IllegalArgumentException("put value can not is null");
        } else if (t instanceof Set) {
            throw new IllegalArgumentException("can not support Set");
        } else {
            TarsOutputStream out = new TarsOutputStream();
            out.setServerEncoding(StandardCharsets.UTF_8);
            out.write(t, 0);
            byte[] data = out.toByteArray();
            if(this.version == 3) {
                this._newData.put(mapName, data);
            } else {
                TarsOutputStream stream = new TarsOutputStream();
                stream.write(t, 0);
                byte[] bytes = stream.toByteArray();
                HashMap<String, byte[]> map = new HashMap<>(1);
                ArrayList<String> list = new ArrayList<>(1);
                map.put(transTypeList(list), bytes);
                this._data.put(mapName, map);
            }
        }
    }

    public <T extends TarsStructBase> T findByClass(String mapName, T base) {
        TarsInputStream input = new TarsInputStream(find(mapName));
        return input.read(base, 0, true);
    }

    public byte[] find(String mapName) {
        if(this._newData.containsKey(mapName)) {
            return this._newData.get(mapName);
        } else {
            Map<String, byte[]> map = this._data.get(mapName);
            for (Map.Entry<String, byte[]> content : map.entrySet()) {
                return content.getValue();
            }
        }
        return null;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public byte[] encode() {
        TarsOutputStream output = new TarsOutputStream();
        if(this.version == 3) {
            output.write(this._newData, 0);
        } else {
            output.write(this._data, 0);
        }

        byte[] data = output.toByteArray();
        this._package.setVersion(3);
        this._package.setFuncName(this.funcName);
        this._package.setServantName(this.servantName);
        this._package.setContext(new HashMap<>());
        this._package.setStatus(new HashMap<>());
        this._package.setBuffer(data);
        byte[] out = _package.toByteArray();
        ByteBuilder builder = new ByteBuilder();
        builder.writeULong(out.length + 4);
        builder.writeBytes(out);
        data = builder.toByteArray();
        builder.close();
        return data;
    }

    public static UniPacket decode(byte[] bytes) {
        UniPacket uniPacket = new UniPacket();
        TarsInputStream input = new TarsInputStream(bytes, 4);
        uniPacket._package.readFrom(input);
        input = new TarsInputStream(uniPacket._package.buffer);
        uniPacket.version = uniPacket._package.getVersion();
        if(uniPacket.version == 3) {
            if (newCache__tempdata == null) {
                newCache__tempdata = new HashMap<>();
                newCache__tempdata.put("", new byte[0]);
            }
            uniPacket._newData = (HashMap<String, byte[]>) input.read(newCache__tempdata, 0, false);
        } else {
            if (cache__tempdata == null) {
                cache__tempdata = new HashMap<>();
                HashMap<String, byte[]> map = new HashMap<>();
                map.put("", new byte[0]);
                cache__tempdata.put("", map);
            }
            uniPacket._data = (HashMap<String, HashMap<String, byte[]>>) input.read(cache__tempdata, 0, false);
        }
        return uniPacket;
    }



    public static String java2UniType(String packageName) {
        switch (packageName) {
            case "java.lang.Integer":
            case "int":
                return "int32";
            case "java.lang.Boolean":
            case "boolean":
                return "bool";
            case "java.lang.Byte":
            case "byte":
                return "char";
            case "java.lang.Double":
            case "double":
                return "double";
            case "java.lang.Float":
            case "float":
                return "float";
            case "java.lang.Long":
            case "long":
                return "int64";
            case "java.lang.Short":
            case "short":
                return "short";
            case "java.lang.Character":
                throw new IllegalArgumentException("can not support java.lang.Character");
            case "java.lang.String":
                return "string";
            case "java.util.List":
                return "list";
            case "java.util.Map":
                return "map";
            default:
                return packageName;
        }
    }

    public static String transTypeList(ArrayList<String> list) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < list.size(); ++i) {
            list.set(i, java2UniType(list.get(i)));
        }
        Collections.reverse(list);
        for(int i = 0; i < list.size(); ++i) {
            String name = list.get(i);
            switch (name) {
                case "list":
                case "Array":
                    list.set(i - 1, "<" + list.get(i - 1));
                    list.set(0, list.get(0) + ">");
                    break;
                case "map":
                    list.set(i - 1, "<" + list.get(i - 1) + ",");
                    list.set(0, list.get(0) + ">");
                    break;
            }
        }
        Collections.reverse(list);
        for (String s : list) {
            builder.append(s);
        }
        return builder.toString();
    }

    public String getServantName() {
        return servantName;
    }

    public void setServantName(String servantName) {
        this.servantName = servantName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
