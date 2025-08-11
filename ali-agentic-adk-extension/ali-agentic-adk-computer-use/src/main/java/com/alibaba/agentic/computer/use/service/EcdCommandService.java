package com.alibaba.agentic.computer.use.service;

import com.alibaba.agentic.computer.use.service.dto.DesktopCommandResponse;
import com.aliyun.ecd20200930.models.CreateCdsFileRequest;
import com.aliyun.ecd20200930.models.DescribeInvocationsRequest;
import com.aliyun.ecd20200930.models.RunCommandRequest;
import com.aliyun.ecd20201002.models.GetLoginTokenRequest;
import com.aliyun.eds_aic20230930.models.DescribeAndroidInstancesRequest;
import com.aliyun.eds_aic20230930.models.DescribeAndroidInstancesResponseBody;

import java.util.List;

public interface EcdCommandService {

    String uploadScript(CreateCdsFileRequest createCdsFileRequest);

    /**
     * 下发脚本命令
     */
    String runCommand(RunCommandRequest request);

    String runCommand(RunCommandRequest request, String regionId);

    List<DesktopCommandResponse> getCommandResult(DescribeInvocationsRequest request);

    List<DesktopCommandResponse> getCommandResult(DescribeInvocationsRequest request, String regionId);

    String getLoginToken(GetLoginTokenRequest request);

    String getAuthCode(String userId);

    List<DescribeAndroidInstancesResponseBody.DescribeAndroidInstancesResponseBodyInstanceModel> getAndroidInstance(DescribeAndroidInstancesRequest request);
}
