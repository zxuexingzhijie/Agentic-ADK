package com.alibaba.agentic.computer.use.configuration;

import com.google.adk.agents.BaseAgent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BrowserAgentRegister {

    private static final Map<String, BaseAgent> agents = new ConcurrentHashMap();

    public BrowserAgentRegister() {
    }

    public static void register(BaseAgent agent) {
        String agentName = agent.name();
        if (agents.containsKey(agentName)) {
            throw new IllegalArgumentException("Agent with name " + agentName + " already registered");
        } else {
            agents.put(agent.name(), agent);
        }
    }

    public static BaseAgent getAgent(String agentName) {
        return (BaseAgent)agents.get(agentName);
    }

    public static Map<String, BaseAgent> getAgents() {
        return agents;
    }

}
