/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.solon.ai.example.manus.dynamic.agent.service;

import org.noear.solon.ai.example.manus.config.ManusProperties;
import org.noear.solon.ai.example.manus.dynamic.agent.DynamicAgent;
import org.noear.solon.ai.example.manus.dynamic.agent.entity.DynamicAgentEntity;
import org.noear.solon.ai.example.manus.dynamic.agent.repository.DynamicAgentRepository;
import org.noear.solon.ai.example.manus.llm.LlmService;
import org.noear.solon.ai.example.manus.recorder.PlanExecutionRecorder;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicAgentLoader {

	private final DynamicAgentRepository repository;

	private final LlmService llmService;

	private final PlanExecutionRecorder recorder;

	private final ManusProperties properties;

	private final ToolCallingManager toolCallingManager;

	public DynamicAgentLoader(DynamicAgentRepository repository, @Lazy LlmService llmService,
			PlanExecutionRecorder recorder, ManusProperties properties, @Lazy ToolCallingManager toolCallingManager) {
		this.repository = repository;
		this.llmService = llmService;
		this.recorder = recorder;
		this.properties = properties;
		this.toolCallingManager = toolCallingManager;
	}

	public DynamicAgent loadAgent(String agentName) {
		DynamicAgentEntity entity = repository.findByAgentName(agentName);
		if (entity == null) {
			throw new IllegalArgumentException("Agent not found: " + agentName);
		}

		return new DynamicAgent(llmService, recorder, properties, entity.getAgentName(), entity.getAgentDescription(),
				entity.getSystemPrompt(), entity.getNextStepPrompt(), entity.getAvailableToolKeys(),
				toolCallingManager);
	}

	public List<DynamicAgentEntity> getAllAgents() {
		return repository.findAll();
	}

}
